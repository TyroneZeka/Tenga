import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { Link } from 'react-router-dom'
import { useRegister } from '@/features/auth'
import { ZIMBABWE_PHONE_REGEX } from '@/lib/constants'

const schema = z.object({
  firstName: z.string().min(1, 'First name is required').max(50, 'First name is too long'),
  lastName: z.string().min(1, 'Last name is required').max(50, 'Last name is too long'),
  phone: z
    .string()
    .min(1, 'Phone number is required')
    .regex(ZIMBABWE_PHONE_REGEX, 'Enter a valid Zimbabwean number — e.g. +263771234567'),
  email: z.string().email('Enter a valid email address').optional().or(z.literal('')),
  password: z
    .string()
    .min(8, 'Password must be at least 8 characters')
    .regex(/[A-Z]/, 'Must contain at least one uppercase letter')
    .regex(/[0-9]/, 'Must contain at least one number'),
})

type FormValues = z.infer<typeof schema>

function apiErrorMessage(err: unknown): string {
  const e = err as { response?: { data?: { message?: string; details?: Record<string, string> } } }
  const data = e?.response?.data
  if (data?.details) {
    return Object.values(data.details).join(' · ')
  }
  return data?.message ?? 'Registration failed. Please try again.'
}

export default function RegisterPage() {
  const { mutate, isPending, error } = useRegister()
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FormValues>({ resolver: zodResolver(schema) })

  function onSubmit(data: FormValues) {
    const { firstName, lastName, ...rest } = data
    mutate({ ...rest, displayName: `${firstName} ${lastName}`.trim(), email: data.email || undefined })
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-[#f5f5f5] px-4">
      <div className="w-full max-w-sm">
        <p className="mb-6 text-center text-3xl font-black italic text-[#ff6000]">Tenga</p>

        <div className="rounded-2xl bg-white p-8 shadow-sm">
          <h1 className="mb-1 text-xl font-bold text-gray-900">Create account</h1>
          <p className="mb-6 text-sm text-gray-500">Join Zimbabwe's marketplace</p>

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div className="grid grid-cols-2 gap-3">
              <Field label="First name" error={errors.firstName?.message}>
                <input
                  {...register('firstName')}
                  className={inputCls(!!errors.firstName)}
                  placeholder="Tinashe"
                  autoComplete="given-name"
                />
              </Field>
              <Field label="Last name" error={errors.lastName?.message}>
                <input
                  {...register('lastName')}
                  className={inputCls(!!errors.lastName)}
                  placeholder="Moyo"
                  autoComplete="family-name"
                />
              </Field>
            </div>

            <Field
              label="Phone number"
              error={errors.phone?.message}
              hint="Used to log in and for mobile money"
            >
              <input
                {...register('phone')}
                type="tel"
                className={inputCls(!!errors.phone)}
                placeholder="+263771234567"
                autoComplete="tel"
              />
            </Field>

            <Field
              label={
                <>
                  Email <span className="font-normal text-gray-400">(optional)</span>
                </>
              }
              error={errors.email?.message}
            >
              <input
                {...register('email')}
                type="email"
                className={inputCls(!!errors.email)}
                placeholder="you@example.com"
                autoComplete="email"
              />
            </Field>

            <Field
              label="Password"
              error={errors.password?.message}
              hint="Min 8 chars · 1 uppercase · 1 number"
            >
              <input
                {...register('password')}
                type="password"
                className={inputCls(!!errors.password)}
                placeholder="••••••••"
                autoComplete="new-password"
              />
            </Field>

            {error && (
              <div className="rounded-lg bg-red-50 px-3 py-2.5 text-sm text-red-700">
                {apiErrorMessage(error)}
              </div>
            )}

            <button
              type="submit"
              disabled={isPending}
              className="w-full rounded-lg bg-[#ff6000] py-2.5 text-sm font-semibold text-white hover:bg-[#e55500] disabled:opacity-50"
            >
              {isPending ? 'Creating account…' : 'Create account'}
            </button>
          </form>

          <p className="mt-5 text-center text-sm text-gray-500">
            Already have an account?{' '}
            <Link to="/login" className="font-semibold text-[#ff6000] hover:underline">
              Sign in
            </Link>
          </p>
        </div>
      </div>
    </div>
  )
}

function inputCls(hasError: boolean) {
  return `w-full rounded-lg border px-3 py-2 text-sm transition focus:outline-none focus:ring-1 ${
    hasError
      ? 'border-red-400 focus:border-red-400 focus:ring-red-300'
      : 'border-gray-300 focus:border-[#ff6000] focus:ring-[#ff6000]/30'
  }`
}

interface FieldProps {
  label: React.ReactNode
  error?: string
  hint?: string
  children: React.ReactNode
}

function Field({ label, error, hint, children }: FieldProps) {
  return (
    <div>
      <label className="mb-1 block text-sm font-medium text-gray-700">{label}</label>
      {children}
      {error ? (
        <p className="mt-1 text-xs text-red-600">{error}</p>
      ) : hint ? (
        <p className="mt-1 text-xs text-gray-400">{hint}</p>
      ) : null}
    </div>
  )
}
