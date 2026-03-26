import { useNavigate, Link } from 'react-router-dom'
import { ChevronRight, Package, ShoppingBag, Star, Edit3, LogOut, Settings } from 'lucide-react'
import { useProfile } from '@/features/user'
import { useUserListings } from '@/features/listings'
import { useAuthStore } from '@/stores/authStore'

export default function ProfilePage() {
  const user = useAuthStore((s) => s.user)!
  const logout = useAuthStore((s) => s.logout)
  const navigate = useNavigate()
  const { data: profile, isLoading } = useProfile(user.id)
  const { data: listingsPage } = useUserListings(user.id)

  const activeListings = listingsPage?.content.length ?? profile?.activeListingCount ?? 0

  function handleLogout() {
    logout()
    navigate('/login', { replace: true })
  }

  const avatarLetter = (profile?.displayName ?? user.displayName ?? 'U').charAt(0).toUpperCase()

  return (
    <div className="min-h-screen bg-[#f5f5f5] pb-20">
      {/* ── Hero header ─────────────────────────────────────────────── */}
      <div className="bg-[#1a1a2e] px-5 pb-6 pt-10">
        <div className="flex items-start justify-between">
          <div className="flex items-center gap-4">
            {/* Avatar */}
            {profile?.avatarUrl ? (
              <img
                src={profile.avatarUrl}
                alt={profile.displayName}
                className="h-16 w-16 rounded-full border-2 border-white/20 object-cover"
              />
            ) : (
              <div className="flex h-16 w-16 items-center justify-center rounded-full border-2 border-white/20 bg-[#ff6000] text-2xl font-bold text-white">
                {isLoading ? '…' : avatarLetter}
              </div>
            )}

            {/* Name + trust */}
            <div>
              <p className="text-base font-bold text-white">
                {profile?.displayName ?? user.displayName ?? '…'}
              </p>
              <p className="mt-0.5 text-xs text-white/50">{user.phone}</p>
              {profile?.trustScore != null && (
                <div className="mt-1.5 flex items-center gap-1">
                  <Star className="h-3 w-3 fill-amber-400 text-amber-400" />
                  <span className="text-xs font-semibold text-amber-400">
                    {Number(profile.trustScore).toFixed(1)}
                  </span>
                  <span className="text-xs text-white/40">trust score</span>
                </div>
              )}
            </div>
          </div>

          {/* Settings gear */}
          <Link to="/settings/profile" className="rounded-full p-1.5 text-white/60 hover:text-white">
            <Settings className="h-5 w-5" />
          </Link>
        </div>

        {/* Stats row */}
        <div className="mt-5 flex divide-x divide-white/10 rounded-xl bg-white/5 py-3">
          <div className="flex flex-1 flex-col items-center">
            <span className="text-lg font-bold text-white">{activeListings}</span>
            <span className="mt-0.5 text-[11px] text-white/50">Listings</span>
          </div>
          <div className="flex flex-1 flex-col items-center">
            <span className="text-lg font-bold text-white">
              {profile?.trustScore != null ? Number(profile.trustScore).toFixed(1) : '—'}
            </span>
            <span className="mt-0.5 text-[11px] text-white/50">Trust</span>
          </div>
          <div className="flex flex-1 flex-col items-center">
            <span className="text-lg font-bold text-white">
              {profile?.memberSince ? new Date(profile.memberSince).getFullYear() : '—'}
            </span>
            <span className="mt-0.5 text-[11px] text-white/50">Since</span>
          </div>
        </div>
      </div>

      {/* ── Menu items ──────────────────────────────────────────────── */}
      <div className="mt-3 overflow-hidden rounded-xl bg-white mx-3 shadow-sm">
        <MenuItem
          icon={<Package className="h-5 w-5 text-[#ff6000]" />}
          label="My Listings"
          to={`/users/${user.id}`}
        />
        <MenuItem
          icon={<ShoppingBag className="h-5 w-5 text-[#ff6000]" />}
          label="My Orders"
          to="/orders"
        />
        <MenuItem
          icon={<Edit3 className="h-5 w-5 text-[#ff6000]" />}
          label="Edit Profile"
          to="/settings/profile"
          last
        />
      </div>

      {/* ── Logout ──────────────────────────────────────────────────── */}
      <div className="mx-3 mt-3">
        <button
          onClick={handleLogout}
          className="flex w-full items-center justify-center gap-2 rounded-xl bg-white py-3.5 text-sm font-semibold text-red-500 shadow-sm hover:bg-red-50 active:bg-red-100"
        >
          <LogOut className="h-4 w-4" />
          Log out
        </button>
      </div>

      {/* App version */}
      <p className="mt-6 text-center text-[11px] text-gray-300">Tenga v0.1.0 · Zimbabwe</p>
    </div>
  )
}

interface MenuItemProps {
  icon: React.ReactNode
  label: string
  to: string
  last?: boolean
}

function MenuItem({ icon, label, to, last }: MenuItemProps) {
  return (
    <Link
      to={to}
      className={`flex items-center gap-3 px-4 py-3.5 transition-colors hover:bg-gray-50 active:bg-gray-100 ${
        last ? '' : 'border-b border-gray-100'
      }`}
    >
      <span className="flex h-8 w-8 items-center justify-center rounded-full bg-orange-50">
        {icon}
      </span>
      <span className="flex-1 text-sm font-medium text-gray-800">{label}</span>
      <ChevronRight className="h-4 w-4 text-gray-300" />
    </Link>
  )
}
